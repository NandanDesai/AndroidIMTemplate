# A Brief Guide for building an Instant Messaging app for Android

## Prerequisite
You need to know the basics of Android app development (like what are Activities, Services etc.)

## Introduction

This repository contains code and resources for creating a secure Instant messaging app. Here, the focus is on some basic functional requirements and more importantly the security requirements of the app. This repository *doesn't* contain the complete code for an instant messaging app but provides a template for you to build upon (hence the name 'AndroidIMTemplate'). You can treat this repo as your reference material. Additionally, it provides you a few resources which can be quite helpful.

![a working example gif](https://raw.githubusercontent.com/NandanDesai/res/master/androidIMTemplate_working.gif)

### Introduction to App Architecture
It is recommended to follow MVVM (Model-View-ViewModel) architectural pattern for building this app. If you don't know what MVVM is, then the following resources could be helpful to understand it:

 - [Video tutorial by CodingWithMitch on YouTube](https://www.youtube.com/watch?v=ijXjCtCXcN4)

There are a few official Android libraries (developed by Google) which are very much suitable for our MVVM architecture. These include the following:

 - Room Persistence Library
 - Lifecycle components (includes ViewModel and LiveData classes)

Now, to get an introduction to the above libraries, you can head over to the following resource:

 - [Video by Android Developers on YouTube](https://www.youtube.com/watch?v=7p22cSzniBM)
 - [A Video tutorial Playlist by Coding In Flow on YouTube](https://www.youtube.com/watch?v=ARpn-1FPNE4&list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118)

Apart from the core architectural components like Room and Lifecycle components, the following libraries are also used in this project:

 - [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview) for displaying lists
 - [Emoji](https://github.com/vanniktech/Emoji) for emoji keyboard
 - [Glide](https://github.com/bumptech/glide) for loading images
 - SQLCipher for encrypted database
 - [SafeRoom](https://github.com/commonsguy/cwac-saferoom) for combining Room Persistence Library with SQLCipher

Here is Google's [recommended way](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) of building the app's architecture (i.e., MVVM). It is depicted the the diagram below: 
![Diagram taken from google's official android docs](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

In this github repo, you can see the packages namely *model*, *repositories*, *viewmodel* which are to adhere with the above architecture. Apart from these, there are also packages like *adapters* (which contain the Adapter classes for RecyclerView and TabLayout) and *database* (which contain the DAOs (Data Access Objects)).

This github repo contains the two important *Activities* namely:
MainActivity and ChatActivity. MainActivity contains two fragments on a TabLayout. These two fragments are ChatListFragment and ContactListFragment.

ChatListFragment is shown below:

![chatlistfragment image](https://raw.githubusercontent.com/NandanDesai/res/master/chatlistactivity.png) 

ChatActivity is shown below:

![chatactivity image](https://raw.githubusercontent.com/NandanDesai/res/master/chatactivity.png)

ChatActivity and ChatListFragment are comparatively harder to design as they contain a lot of moving parts. You can refer those ChatActivity.java and ChatListFragment.java and their related xml files for designing the UI of your app. I'm not mentioning other Activities and Fragments used in this repo because you can build those according to your own requirements. 

### Network Architecture and Protocols
...
