# CS-360 Mobile Architecture and Programming

The goal of this project was to design from start to finish an event tracking app. The goal of this app is to allow users to create and track upcoming events with the ability to opt into SMS messaging on the day of the event.
The app requirements were pretty general only requiring the ability for users to create an account, log into their account, and CRUD (create, read, update, destroy) interactions with a custom database for event informations
Implementation of the design was based on two competitor apps, *Days Counter* by Nettlebit and *Periodically* by TimeTune Studio. 

### **FUNCTIONALITY**
* Create User
* Login Existing User
* Add new Events
* Edit existing Events
* Delete existing Events
* Displays Events with recycler grid view

### **TOOLS**
* Android Studio
* Java (code)
* Lucid Chart (Wireframes)

The app consists of a login screen which allows both creation of accounts and logging in for returning users, a main screen with a recycler grid view of events including a FAB to create a new event, or long-clicking an existing-event
to edit. A third screen allows creation, edits, and deletion of events using event title, description, time, and date fields. A final screen was implemented to prompt for user-permission to send SMS notifications as event reminders.

This was my first project built using Android Studio, initial challenges including familiarizing myself with not only Android development but how to use the tools to do so as well. This project was designed and developed over an eight-week
semester. Initial mockups of screens were sketched and tested out in Android Studio before submitting designs to stakeholders for feedback. UI designs were adapted and improved in response to feedback, room for improvements in 
current design include clarifying new-user registration flow, and adapting designs to fit a larger variety of screen sizes.  

Development was incremental, establishing base functionality for each use-case with subsequent improvements until total functionality and testing were accomplished. Due to the limited time of the project, test coverage is not
as thorough as ideal however base functionality has been verified. Thorough use of both commenting and logging helped with debugging and organizing the project and will aid in future projects. Establishing and adhering stricter
use-cases for test-driven development strategies will benefit future designs. 

Key highlight of this app is use of the recycler grid view for displaying an array list of custom Event objects on the main screen of the app. Database interactions are all run on background asynchronous thread tasks to not slow 
down main processeses. This project serves as a starting point for future Android designs and was a great learning experience. 


### **WIREFRAMES**
![LogIn](https://github.com/sacredpoom/Mobile-Architect-Programming/assets/20672168/1bdb1f96-64a0-462c-9d35-7e0a59a3773c)


![Main Screen](https://github.com/sacredpoom/Mobile-Architect-Programming/assets/20672168/a7eb4eca-788e-4f13-b945-8feab285a644)

### **FINAL SCREENS**

![loginScreen](https://github.com/sacredpoom/Mobile-Architect-Programming/assets/20672168/b893e493-55dc-46d4-b5b4-c33c6983fc53)

![MainScreen](https://github.com/sacredpoom/Mobile-Architect-Programming/assets/20672168/18468dc6-4c08-46f3-9e87-50b47a951268)

![EditEventScreen](https://github.com/sacredpoom/Mobile-Architect-Programming/assets/20672168/9991b849-eacf-4510-84b9-d13bbe8a668e)

![NotificationScreen](https://github.com/sacredpoom/Mobile-Architect-Programming/assets/20672168/88304235-9453-4edc-a26e-415c494f0c93)
