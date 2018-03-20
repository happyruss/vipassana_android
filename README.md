vipassana_andriod
=============

This is the actual code used by the Vipassana App from Guided Meditation Treks! Info about the background of the Vipassana App can be found at http://www.guidedmeditationtreks.com/vipassana.html The only thing not included in this code is the actual audio assets, which are copyright © by the Author, Russell Eric Dobda.

However, this code is open source for a reason -- it allows other meditation practitioners to use it to create their own guided meditation apps. This app provides you with a framework where you can "plug and play" your own audio tracks into it and present your own guided meditations.

Besides providing your own audio, and your own graphics, these are the only code changes you need to do to make this app your own.

1. Pull down the code locally
2. Open it in Android Studio
3. Dump your audio assets in the /app/src/main/res/raw folder (use ogg files; you can have 1 or 2 parts for each meditation... see below for details)

Change the namespaces everywhere
===================
You need to do a global find/replace for com.guidedmeditationtreks.vipassana and change it to your own namespace. Google requires that this be unique, and I've already got this namespace, so do something like com.yourcompany.yourapp

Make modifications to TrackTemplateFactory.java like so:
======================
```
public class TrackTemplateFactory {

    public static TrackTemplateFactory singleton = new TrackTemplateFactory();

    private TrackTemplate[] trackTemplates;

    private boolean requireMeditationsBeDoneInOrder = true; //< Change if you don't require meditations be done in order

    public TrackTemplateFactory() {

        trackTemplates = new TrackTemplate[11]; //<<<< Change this to the number of tracks

        trackTemplates[0] = new TrackTemplate( "Silent Meditation", "Silent Meditation", R.raw.bellstarting, R.raw.bellclosing); //<< You should always have this track for the silent timer

        trackTemplates[1] = new TrackTemplate("Introduction", "Introduction", R.raw.introduction, 0); //<< this is an example of a track with only part 1

        trackTemplates[2] = new TrackTemplate("Shamatha", "Shamatha",R.raw.shamatha, R.raw.shamatha2);  //<< this is an example of a track with two parts
 ...

```
The fields in the TrackTemplate constructor are pretty self explanatory:
- name: the name that appears in the button
- longName: the name that appears on the meditation screen
- part1Resource: the audio resource for the first part
- part2Resource: the audio resource for the second part, if applicable.


Make modifications to strings.xml like so:
===============

```
    <string name="app_name">Change to your app name</string>
    <string name="url">http://Your url</string>

```

Use your own images!!!
=========================
  1. Drawable/LaunchLogo.jpg - use your own
  2. Mipmap/background.jpg - use your own!
  3. gmt_logo_white.jpg - use your own logo!
  4. ic_launcher.jpg - these are copyright, use your own!
  5. buddha.png - this is the GMT logo, copyright! Use your own logo!


Please use your own graphics and intro screen!!!!
=================================================

If you decide to use this open source code, DO NOT make it look like it is from Guided Meditation Treks. Make it your own! If you want to give a shout out that you used my code, that's great, but the audio assets are all yours, and if it looks like it's from me, I'll submit a complaint for infringement. This also goes for the assets and the use of the GMT logo. This means, you should change ALL of the assets located within mipmap folder to use your own, and change the activity XML's to point to YOUR assets, not mine!

If you like the graphic design and UX design in this app, contact their maker, Daniel Boros at http://www.danielboros.com to have him create some assets for you! Please do not re-use the ones in this project, as they are copyright for this project.

Feel free to use any of this code as you see fit, but ....

Failure to respect my copyrights, logos, or designs will result in bad karma!
=============================================================================
