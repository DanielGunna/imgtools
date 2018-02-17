# ImgTools

Created by: [Gustavo Araújo](https://github.com/GustavoHGAraujo) (gustavo.hg.araujo@gmail.com)

## Features

This module has already implemented the functions and procedures related to images that are currently used in Android apps, such as:

- Download images using a URL
- Setting images into ImageView (when a ProgressBar is passed with the ImageView, it is automatically hidden when the image is visible)
- Crop image into square and circle shapes

## How to use

To import this module simply:

1. Open the Android Studio
2. On the menu `File`, select `New` then `Import module`
3. In the `Source directory` find the `imgtools` directory and then click in `Finish`
4. Add `compile project(path: ':imgtools')` to the `dependencies` section of the `build.gradle` of the module that will use ImgTools.
5. Done. Now import the class into your Java classes.

Because all the functions are static, to use them simply call using `com.gustavogoma.utils.imgtools.ImgTools.<method_name>`
