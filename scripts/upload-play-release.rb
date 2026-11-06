#!/usr/bin/env ruby
# frozen_string_literal: true

require "json"
require "google/apis/androidpublisher_v3"

$stdout.sync = true

repo_root = File.expand_path("..", __dir__)
Dir.chdir(repo_root)

track = "internal"
promote_only = ARGV.delete("--promote-only")
track_arg = ARGV.find { |a| a.start_with?("--track=") }
if track_arg
  track = track_arg.split("=", 2)[1]
  ARGV.delete(track_arg)
end
aab = File.expand_path(ARGV[0] || "app/build/outputs/bundle/release/app-release.aab")
pkg = "com.guidedmeditationtreks.vipassana"
json_key = File.expand_path(ENV.fetch("PLAY_STORE_JSON_KEY", "~/.playstore/service-account.json"))
gradle_file = File.join(repo_root, "app/build.gradle")

abort("Missing Play credentials at #{json_key}") unless File.exist?(json_key)
abort("Missing #{gradle_file}") unless File.exist?(gradle_file)
unless promote_only
  abort("Missing AAB at #{aab}") unless File.exist?(aab)
end

version = File.read(gradle_file)[/versionCode\s+(\d+)/, 1].to_i
version_name = File.read(gradle_file)[/versionName\s+"([^"]+)"/, 1] || "1.5"
abort("Could not read versionCode from app/build.gradle") if version.zero?

unless %w[internal production].include?(track)
  abort("Unsupported track #{track.inspect} — use internal or production")
end

service = Google::Apis::AndroidpublisherV3::AndroidPublisherService.new
service.client_options.open_timeout_sec = 60
service.client_options.read_timeout_sec = 3600
service.client_options.send_timeout_sec = 3600
service.authorization = Google::Auth::ServiceAccountCredentials.make_creds(
  json_key_io: File.open(json_key),
  scope: "https://www.googleapis.com/auth/androidpublisher",
)

puts "Creating edit..."
begin
  edit_id = service.insert_edit(pkg).id
rescue Google::Apis::ClientError => e
  if e.message.include?("permission") || (e.respond_to?(:status_code) && e.status_code == 403)
    email = JSON.parse(File.read(json_key))["client_email"]
    abort <<~MSG
      Play API permission denied for #{pkg}.

      The service account (#{email}) can upload to other apps in this project, but is not granted access to Vipassana in Play Console.

      Fix: Play Console → Settings → API access → #{email} → Grant access → select Vipassana
      (com.guidedmeditationtreks.vipassana) with permission to release to testing tracks (or Admin).

      Or set PLAY_STORE_JSON_KEY to a JSON key for an account that already has Vipassana access.
    MSG
  end
  raise
end
puts "Edit: #{edit_id}"

unless promote_only
  size_mb = (File.size(aab) / 1024.0 / 1024.0).round(1)
  puts "Uploading #{size_mb} MB AAB..."
  start = Time.now
  bundle = service.upload_edit_bundle(
    pkg,
    edit_id,
    upload_source: aab,
    content_type: "application/octet-stream",
    ack_bundle_installation_warning: true,
  )
  elapsed = (Time.now - start).round(1)
  puts "Uploaded versionCode=#{bundle.version_code} in #{elapsed}s"
end

puts "Updating #{track} track (completed)..."
track_obj = Google::Apis::AndroidpublisherV3::Track.new(
  track: track,
  releases: [
    Google::Apis::AndroidpublisherV3::TrackRelease.new(
      name: "#{version_name} (#{version})",
      status: "completed",
      version_codes: [version],
    ),
  ],
)
service.update_edit_track(pkg, edit_id, track, track_obj)

puts "Committing edit..."
service.commit_edit(pkg, edit_id)
puts "Done — version #{version_name} (#{version}) is live on #{track}."
