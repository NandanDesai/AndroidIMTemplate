
# A brief guide on building an Instant Messaging app for Android

## Prerequisite
You need to know the basics of Android app development (like what are Activities, Services etc.)

## Introduction

This repository contains code and resources for creating a secure Instant messaging app. Here, the focus is on some basic functional requirements and more importantly the security requirements of the app. This repository *doesn't* contain the complete code for an instant messaging app but provides a template for you to build upon (hence the name 'AndroidIMTemplate'). You can treat this repo as your reference material. Additionally, it provides you a few resources which can be quite helpful.

![a working example gif](https://raw.githubusercontent.com/NandanDesai/res/master/androidIMTemplate_working.gif)

## App Architecture
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

Here is Google's [recommended way](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) of building the app's architecture (i.e., MVVM). It is depicted in the diagram below: 
![Diagram taken from google's official android docs](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

In this github repo, you can see the packages namely *model*, *repositories*, *viewmodel* which are to adhere with the above architecture. Apart from these, there are also packages like *adapters* (which contain the Adapter classes for RecyclerView and TabLayout) and *database* (which contain the DAOs (Data Access Objects)).

This github repo contains the two important *Activities* namely:
MainActivity and ChatActivity. MainActivity contains two fragments on a TabLayout. These two fragments are ChatListFragment and ContactListFragment.

ChatListFragment is shown below:

![chatlistfragment image](https://raw.githubusercontent.com/NandanDesai/res/master/chatlistactivity.png) 

ChatActivity is shown below:

![chatactivity image](https://raw.githubusercontent.com/NandanDesai/res/master/chatactivity.png)

ChatActivity and ChatListFragment are comparatively harder to design as they contain a lot of moving parts. In this repo, you can refer ChatActivity.java and ChatListFragment.java files and their related xml files for designing the UI of your app. I'm not mentioning other Activities and Fragments used in this repo because you can build those according to your own requirements. 

If you don't want to design your layouts from scratch, then [this](https://github.com/jpush/aurora-imui) library may come in handy!

## Choosing a communication protocol
The communication protocol that you choose for your app depends the network architecture you want to adopt. There are 3 types of network architectures you can adopt while building your Instant Messaging app:

 1. Centralized
 2. Decentralized
 3. Peer-to-Peer


### Centralized architecture
This is a client-server architecture where a central authority is in control of the server. Apps that are based on centralized architecture are WhatsApp, Telegram etc. [XMPP](https://xmpp.org/about/) is a popular communication protocol for instant messaging that supports centralized architecture.

### Decentralized architecture
This is also a client-server architecture but there is no central authority in control. That means, you can have multiple servers controlled by multiple entities. Anyone will be able to setup a server in this architecture. Apps that are based on decentralized architecture are [Xabber](https://www.xabber.com/). 
XMPP can be used for both centralized as well as decentralized architecture. [Matrix](https://www.matrix.org/) is another upcoming decentralized communication protocol.

### Peer-to-Peer architecture
In this architecture, there is no server at all. Clients (here known as Peers) talk to each other directly. Instant messaging communication protocols that are based on peer-to-peer architecture are [Tox](https://tox.chat/) and [Ricochet](https://ricochet.im/).

##### XMPP
If you want to adopt a centralized or decentralized architecture, then XMPP is highly recommended. XMPP has become an industry standard for instant messaging applications. [Smack](https://github.com/igniterealtime/Smack) is an XMPP client library for Java/Android. Check out it's tutorial [here](https://www.baeldung.com/xmpp-smack-chat-client). For XMPP server, there are several implementations of that too! I would recommend checking out [Openfire](https://www.igniterealtime.org/projects/openfire/).

#### Your own custom communication protocol
Although not recommended, you can still choose to implement your own communication protocol. You can build your own protocol on top of [WebSockets](https://blog.teamtreehouse.com/an-introduction-to-websockets) and HTTP. You will have to write your own server and it will be a lot of work but a great learning experience!

## Security requirements

The most important aspect of any instant messaging app is to be secure and respect user's privacy. Getting the cryptography right is extremely difficult and hence you need to follow certain standard procedures and security protocols and not attempt to make your own security protocol.

**#1 E2E encryption**

Nowadays, every instant messaging app needs to have E2E encryption. The protocol that has become an industry standard for E2E encryption in instant messaging apps is ***Signal Protocol***. WhatsApp also uses Signal Protocol for E2E encryption. In order to use this protocol in your app, you need to know how the protocol works. That's very important. 

Signal Protocol was earlier known as TextSecure protocol. Here are some resources that will be helpful to you in understanding how the protocol works:

 - [A talk by Trevor Perrin on YouTube](https://www.youtube.com/watch?v=7WnwSovjYMs)
 - [WhatsApp's technical whitepaper on encryption in their app \[PDF file\]](https://scontent.whatsapp.net/v/t61/68135620_760356657751682_6212997528851833559_n.pdf/WhatsApp-Security-Whitepaper.pdf?_nc_ohc=R4GTDoDjej4AX_kHCH8&_nc_ht=scontent.whatsapp.net&oh=41a89dbef489984653d26b9f1f4ca966&oe=5E2B56A5)
 - [My another github repo demonstrating how to use Signal Protocol java library](https://github.com/NandanDesai/DemoSignal)

You can go through the above links in that given order. 

If you are going to use XMPP protocol, then can switch to [OMEMO](https://en.wikipedia.org/wiki/OMEMO) protocol which is an extension of XMPP protocol and uses Signal Protocol for E2E encryption. [Smack library supports OMEMO](https://github.com/igniterealtime/Smack/blob/master/documentation/extensions/omemo.md).  Openfire XMPP server also has OMEMO support.

**#2 Database encryption**

E2E encryption takes care of encrypting the message when it's in transit from point A to point B over the network. Once the message reaches point B, it will be decrypted back to plain text. Storing the message in plain text on point B's device defeats the whole purpose of using E2E encryption if someone can just steal the device directly and read the message. That is why we need to use an encrypted local database to store messages on the client's device. 

As mentioned earlier in this guide, we need to use SQLCipher encrypted database to store our messages. We will obviously need a key to encrypt/decrypt our database. In this repo, I've hardcoded the key as you can see [here](https://github.com/NandanDesai/AndroidIMTemplate/blob/1e2a7f7ed9b21de453142182a4d32c31842c93b2/app/src/main/java/io/github/nandandesai/android_im_template/database/AndroidIMTemplateDatabase.java#L33). This should never be done as anyone can easily decompile your APK file and read the key. So, we need to generate the key randomly and dynamically and store that key somewhere safe on the device.

For generating and storing the key, you can use [Android keystore system](https://developer.android.com/training/articles/keystore). Following resources can be helpful to understand how to use Android keystore:

 - [A talk by Frederik Schweiger on YouTube](https://www.youtube.com/watch?v=UIyfKBf-ais)
 - [Refer how Signal's Android app is doing this](https://github.com/signalapp/Signal-Android/blob/master/app/src/main/java/org/thoughtcrime/securesms/crypto/KeyStoreHelper.java)

## Conclusion
This guide barely scratched the surface but gave you a good start! Instant messaging apps now have features like Voice calls, Video calls etc. and facilitate the flow of billions of messages per minute! Building that kind of infrastructure is hard. But the journey to get there involves a lot of learning! And that is what makes building projects like this more exciting!

## License
Copyright 2020 Nandan Desai

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
