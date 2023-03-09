
# Cards & Monsters

[![Android Version](https://badgen.net/badge/icon/33?icon=android&label=Android)](https://support.google.com/android/answer/7680439?hl=en)
[![stars](https://badgen.net/github/stars/ohuji/Cards-Monsters)](https://github.com/ohuji/Cards-Monsters)


This app allows you to battle against virtual monsters using decks that you create. With each battle you track your progress, and unlock achievements.

To get started, you'll need to create a deck of cards, each with its unique abilities and attributes. As you battle, you can use these cards to fight against the monsters you encounter.

To battle a monster, you'll need to be in the vicinity of its location, as indicated on the app's map view. Once you're close enough, you can scan for monsters and begin the battle.

As you battle against more monsters, you'll be able to track your progress and achievements through the app's collections feature. This feature allows you to track your progress towards completing different challenges.

# Application screens
<table>
  <tr>
<td><img src="https://user-images.githubusercontent.com/95070775/223972940-e535f7b8-e564-4675-9206-6a90602e41b6.JPG" width="300"></td>
<td><img src="https://user-images.githubusercontent.com/95070775/223974673-4f0b4a6d-311e-431a-a2d3-306097aec900.JPG" width="300"></td>
<td><img src="https://user-images.githubusercontent.com/95070775/223974719-4a9a09a9-3a78-4833-9b31-b4fdd606c27a.JPG" width="300"></td>
</tr>
  <tr>
<td><img src="https://user-images.githubusercontent.com/95070775/223974779-8569666a-ae93-4a49-be4e-4eb0a7c9337f.JPG" width="300"></td>
<td><img src="https://user-images.githubusercontent.com/95070775/223974801-1dbadcca-7455-495f-9dc5-4f7630c8b115.JPG" width="300"></td>
<td><img src="https://user-images.githubusercontent.com/95070775/223974849-d3d5eed6-cab8-4196-a217-84f6fef11f0a.JPG" width="300"></td>
  </tr>
</table>

## Technologies Used

- Kotlin - Official programming language for Android development
- Jetpack Compose - Recommended modern toolkit for building UI
- Compose Material 3 - Design system that provides a set of pre-designed UI components and guidelines
- GitHub Actions - Automated tests
- jUnit - Unit Testing
- SQLite - Database implementation
- Retrofit - Network implementation
- SceneView(Incl. Google Filament & ARCore) - Augmented Reality features
- Google Maps - Map implementation



## Architecture

This app uses MVVM (Model View View-Model) architecture.

![MVVM architecture image](https://miro.medium.com/v2/resize:fit:640/0*EBMAxD0WE23_tZBb)

- Source: https://miro.medium.com/v2/resize:fit:640/0*EBMAxD0WE23_tZBb
## Environment Variables & GitHub Secrets

To run this project, you will need to add the following environment variables to your local.properties file and for our GitHub Actions automated testing to work, also add it to your GitHub secrets.
[How to get a Google Maps API Key.](https://developers.google.com/maps/documentation/embed/get-api-key)

`GOOGLE_MAPS_API_KEY`

## Run Locally

This application doesn't currently have a production release.

- Clone the project

```bash
  git clone https://github.com/ohuji/Cards-Monsters.git
```
- Download & open Android Studio editor

- Augmented reality features can only be used on a physical android phone and not on emulator

- Configure your android mobile phone for dev usage,
more info on: [Run apps on a hardware device](https://developer.android.com/studio/run/device)

- Choose your device instead of emulator

- To build and run your code, choose Run > Run 'app', or click the Run button in your project’s toolbar


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



