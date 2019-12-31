# A Comprehensive Guide for building an Instant Messaging app for Android

## Prerequisite
You need to know the basics of Android app development (like what are Activities, Services etc.)

## Introduction

This repository contains code and resources for creating a secure Instant messaging app. Here, the focus is on some basic functional requirements and more importantly the security requirements of the app. This repository *doesn't* contain the complete code for an instant messaging app but provides a template for you to build upon (hence the name 'AndroidIMTemplate'). Additionally, it provides you a few resources which can be quite helpful.


### Introduction to App Architecture
We will be following MVVM (Model-View-ViewModel) architectural pattern for building this app. If you don't know what MVVM is, then the following resources could be helpful to understand it:

 - [Video tutorial by CodingWithMitch on YouTube](https://www.youtube.com/watch?v=ijXjCtCXcN4)

We will be using a few official Android libraries (developed by Google) which are very much suitable for our MVVM architecture. These include the following:

 - Room Persistence Library
 - Lifecycle components (includes ViewModel and LiveData classes)

Now, to get an introduction to the above libraries, you can head over to the following resource:

 - [Video by Android Developers on YouTube](https://www.youtube.com/watch?v=7p22cSzniBM)
 - [A Video tutorial Playlist by Coding In Flow on YouTube](https://www.youtube.com/watch?v=ARpn-1FPNE4&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118)

Apart from the core architectural components like Room and Lifecycle components, we will also be using the following libraries as well:

 - [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview) for displaying lists
 - [Emoji](https://github.com/vanniktech/Emoji) for emoji keyboard
 - [Glide](https://github.com/bumptech/glide) for loading images
 - SQLCipher for encrypted database
 - [SafeRoom](https://github.com/commonsguy/cwac-saferoom) for combining Room Persistence Library with SQLCipher
