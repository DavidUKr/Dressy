# Dressy

## Description

### Marketing skit 1

A shopping companion that helps visualize your future outfit by generating photos of recommended clothing article combinations. 

Just take your phone out and make a picture to unveil endless possibilities and what the fashion future holds for you.

Or just enter the app and select a photo to upload and your perfect outfit pops up just like that. 

Are you ready to revolutionize your shopping experience, doing what you love most: SHOPPING!

### Marketing skit 2

HEY! 

You’re a boy and you think you have nothing to do with shopping? Does entering a clothing store give you headaches??

THAT CHANGES NOW!!!

Take your phone out and let us help you decide faster, just like a MAN!

Install DRESSY and go with the flow of fashion!!

## Initial Requirements

### Stories:

As a user and I want to take a picture with a clothing article and upload it the app. It should generate some photos containing an outfit with my article and some other clothings which mach it.

As a user I want to set my gender.

As a user and I want to select the preferred style. 

As a user and I want to log in and save my preferred generated outfits to recall the later. 

As the app dev and I want to receive user feedback on ai generations.

- Log in
    - Outsource some service
- Upload image with a clothing article
    - Implement upload box/button
    - Implement uploading mechanism
- Get generated images with matching clothes
    - Implement AI prompting and response generation
    - Display generation
- Style selector
    - Dropdown list selector / user typed style (textbox)
    - Implement prompt with style input
- Gender setting
    - Sing up set gender
    - Implement prompt with gender input
- Save suggestions
    - link a database
    - build a storage architecture
    - manage querying
- User feedback
    - selectable star system
    - textbox
    - save reviews to database
    - build review viewing page
  

## High-level Architecture
https://www.figma.com/board/vjOqurIxbn1jn9veD8VGMz/Dressy-Sys-Arch?node-id=0-1&node-type=canvas&t=KnRaNjRXodFFQzP1-0

## Wireframe
https://www.figma.com/design/WGOslAQ9m63XhRCnfnlPel/Dressy-Wireframe?node-id=0-1&node-type=canvas&t=gQ38vqrrQpey1mpl-0

## Endpoints

UserController:
    -registeruser
    -getuser
    -deleteUser
    -getuserbyId
    -updateUser
GenerationController:
    -getgeneration(salveaza automat poza) 
    -saveGeneration
    -deleteGeneration?
ReviewController:
    -postReview
    -getReview (get by id etc) 
    -deleteReview
