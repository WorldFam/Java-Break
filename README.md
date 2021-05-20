<img align="right" src="https://user-images.githubusercontent.com/55923499/111072954-192dc180-84dd-11eb-9b0a-68fba3c4727b.png">

# Java-Break
Taking breaks is an important part of daily life. First of all, having regular breaks can reduce or prevent even from being stressed. Secondly, it is important to have breaks during work time, because it can improve the overall process and help to concentrate easier. Furthermore, taking a break to stretch after a long time of sitting by your computer, can help you stay healthy and will also give your eyes a needed break.

Having this in mind I decided to create an application that would help to improve daily life and make it easier for people to track their break times. 
For the current solutions, there is no efficient way to keep track of when to take a break, because there is no integrated tool in android. For the alternative, it is possible to set a bunch of alarm clocks, but it is impractical. Already existing applications with a similar concept are missing intuitive design, include millions of ads or are paid, and are outdated.

Java Break; application includes all useful features with a user-friendly interface. A user can choose between two different types of breaks:  productive (sets break remainder for selected time interval) and daily (sets break reminder intervals for the selected day of the week and time). A User will able to choose from the different options of getting a notification: sound, vibration, led. At the same time, the user will get a message with useful information to stay healthy. 



# MoSCoW requirements for the "Java Break;" application:

### Must have
- [x] As a user, I must be able to get a notification to take a break.
- [x] As a user, I must be able to set how often I wanna have a break reminder in minutes and/or hours.
- [x] As a user, I must be able to choose from different types of break reminders (productive, daily).
### Should have
- [x] As a user, I should be able to create a new break reminder by selecting the day of the week and time when I want my break reminder interval to be. 
- [x] As a user, I should be able to choose between different options to notify me (sound, vibration, led ).
- [x] As a user, I should be able to see the list of daily break reminders.
- [x] As a user, I should be able to remove or update the daily break reminder.
### Could have
- [ ] As a user, I could receive a message with different stretch exercises or other useful information when receiving a break reminder.
- [x] As a user, I could set snooze and break duration as an additional option in the break reminder.
- [x] As a user, I could customize notifications and select preferred sounds.
### Will not have
- [ ] As a user, I will be able to receive notifications on my smartwatch. 


# Final thoughts
Link to the video: https://youtu.be/U1V_FFunQUc 

Approximate amount of hours spend on analysis, research, design, and coding: **350h**

In general, the majority of requirements were completed, and fully functional, although not properly tested. App itself is built for Android API level 26+ and is a single activity, which in my case was unnecessary because it brought a lot of additional work and even made the project more complicated. 

As for other technical aspects used SharedPreference to store the time when the application stops and retrieve it, same goes for the configurations page and settings, where I saved the state of switches, sliders and etc. SQLite with a Room framework that was utilized to keep up to date the recycler-view with scheduled reminders. Also, I used viewModel to communicate between the fragments and activity. 

Probably the most time-consuming and complicated part was the notification. As I wanted to create an application for Android +26, it had a lot of restrictions and user-permission allowed features. For this reason, new notification channels were created each time user would change settings, and old ones would be deleted.

##  Screenshots
<table>
  <tr>
    <td><img src="https://user-images.githubusercontent.com/55923499/118409421-99fa6c80-b68a-11eb-9003-2949b570eed0.png" /></td>
    <td><img src="https://user-images.githubusercontent.com/55923499/118409434-aed70000-b68a-11eb-92ee-7e8bc8ad0a00.png" /></td>
    <td><img src="https://user-images.githubusercontent.com/55923499/118409443-c2826680-b68a-11eb-8301-89be378ed827.png" /></td>
    <td><img src="https://user-images.githubusercontent.com/55923499/118409453-ce6e2880-b68a-11eb-9e89-a5c7429d1044.png" /></td>
  </tr>  
  <tr>
    <td><img src="https://user-images.githubusercontent.com/55923499/118409459-d8902700-b68a-11eb-91a0-5475e6b9ccdc.png" /></td>
    <td><img src="https://user-images.githubusercontent.com/55923499/118409934-31f95580-b68d-11eb-98d9-e0516ed2332f.png" /></td>
        <td><img src="https://user-images.githubusercontent.com/55923499/118944395-d103a300-b954-11eb-829a-f1da2f36d5b7.png" /></td>
  </tr>
</table>
