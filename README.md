
# Cards & Monsters

[![Android Version](https://badgen.net/badge/icon/33?icon=android&label=Android)](https://support.google.com/android/answer/7680439?hl=en)
[![stars](https://badgen.net/github/stars/ohuji/Cards-Monsters)](https://github.com/ohuji/Cards-Monsters)


This app allows you to battle against virtual monsters using decks that you create. With each battle you track your progress, and unlock achievements.

To get started, you'll need to create a deck of cards, each with its unique abilities and attributes. As you battle, you can use these cards to fight against the monsters you encounter.

To battle a monster, you'll need to be in the vicinity of its location, as indicated on the app's map view. Once you're close enough, you can scan for monsters and begin the battle.

As you battle against more monsters, you'll be able to track your progress and achievements through the app's collections feature. This feature allows you to track your progress towards completing different challenges.



## Technologies Used

- Kotlin - Official programming language for Android development
- Jetpack Compose - Recommended modern toolkit for building UI
- Compose Material 3 - Design system that provides a set of pre-designed UI components and guidelines
- GitHub Actions - Automated tests
- jUnit - Unit Testing
- SQLite - Database implementation
- Rerofit - Network implementation
- SceneView(Incl. Google Filament & ARCore) - Augmented Reality features
- Google Maps - Map implementation



## Architecture

This app uses MVVM (Model View View-Model) architecture.

![MVVM architecture image](https://miro.medium.com/v2/resize:fit:640/0*EBMAxD0WE23_tZBb)

- Source: https://miro.medium.com/v2/resize:fit:640/0*EBMAxD0WE23_tZBb
## Environment Variables & GitHub Secrets

To run this project, you will need to add the following environment variables to your local.properties file and for our GitHub Actions automated testing to work, also add it to your GitHub secrets

`GOOGLE_MAPS_API_KEY`

## Run Locally

Application doesn't currently have a production release

Clone the project

```bash
  git clone https://github.com/ohuji/Cards-Monsters.git
```
Download & open Android Studio editor

Augmented reality features can only be used on a physical android phone and not on emulator.
Configure your android mobile phone for dev usage. 
More info on: [Run apps on a hardware device](https://developer.android.com/studio/run/device)
Choose your device instead of emulator.
To build and run your code, choose Run > Run 'app', or click the Run button in your project’s toolbar.


## License

```
MIT License

Copyright (c) 2023 Juho Salomäki

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```


## Authors

- [@IlmHe](https://github.com/IlmHe)
- [@jannhakk](https://github.com/jannhakk)
- [@ohuji](https://github.com/ohuji)
- [@Aaljani](https://github.com/Aaljani)


## Links
- [GoT API](https://gameofthronesquotes.xyz/)
- [SceneView](https://github.com/SceneView/sceneview-android)



