# -*- coding: utf-8 -*-
import os
import shutil
import sys

import pyperclip
import requests
from bs4 import BeautifulSoup

url = ""
headers  = {}
response = None

def print_sep():
    print("{0:-<100}".format(""))

def filter(td, page_value, pes_editor_value = ""):
    if pes_editor_value == "":
        pes_editor_value = page_value
        
    if page_value in td[1].text:
        return pes_editor_value + ": " + td[0].findChildren("span" , recursive=False)[0].text + "\n" 
    
    return ""

def get_pes2018_player_skills(skill):
    player_skills = [
        "01 - Scissor Feint",
        "02 - Flip Flap",
        "03 - Marseille Turn",
        "04 - Sombrero",
        "05 - Cut Behind & Turn",
        "08 - Long Range Drive",
        "09 - Knuckle Shot",
        "10 - Acrobatic Finish",
        "11 - Heel Trick",
        "12 - First-time Shot",
        "14 - Weighted Pass",
        "15 - Pinpoint Crossing",
        "16 - Outside Curler",
        "19 - Low Punt Trajectory",
        "20 - Long Throw",
        "21 - GK Long Throw",
        "22 - Malicia",
        "23 - Man Marking",
        "24 - Track Back",
        "26 - Captaincy",
        "27 - Super-Sub",
        "28 - Fighting Spirit"
    ]

    skill = skill.replace(" ", "").lower()

    for s in player_skills:
        if skill in s.replace(" ", "").lower():
            return s
    
    return ""

def get_pes2018_com_skills(skill):
    com_skills = [
        "01 - Trickster",
        "02 - Mazing Run",
        "03 - Speeding Bullet",
        "04 - Incisive Run",
        "05 - Long-Ball Expert",
        "06 - Early Cross",
        "07 - Long Ranger"
    ]

    skill = skill.replace(" ", "").lower()

    for s in com_skills:
        if skill in s.replace(" ", "").lower():
            return s
    
    return ""

def search_pes_master(url):    
    headers = {"User-Agent":"Mozilla/5.0"}

    response = requests.get(url, headers=headers)
    
    soup = BeautifulSoup(response.text, 'lxml')

    output = ""

    n = soup.select('.top-header')[0].findChildren("span" , recursive=True)[1].text
    n = n.upper()
    
    output += "Name: " + n + "\n" + "Shirt Name: " + n + "\n"

    # info
    for tr in soup.select('.player-info > tbody tr'):
        td = tr.findChildren("td" , recursive=False)
        
        if "Nationality" in td[0].text: 
            output += "Nationality: " + td[1].text + "\n"
            
        if "Age" in td[0].text: 
            output += "Age: " + td[1].text + "\n"
            
        if "Height" in td[0].text: 
            output += "Height: " + td[1].text + "\n"
            
        if "Weight" in td[0].text: 
            output += "Weight: " + td[1].text + "\n"  
            
        if "Stronger Foot"  in td[0].text: 
            output += "Foot: " + ("R" if "Right" in td[1].text else "L") + "\n"
        
        if "Position" in td[0].text:
            output += "Registered Position: " + td[1].findChildren("span" , recursive=False)[0].text + "\n" 
        
    # stats
    for tr in soup.select('.player-stats-modern > tbody tr'):
        td = tr.findChildren("td" , recursive=False)
        
        output += filter(td, "Weak Foot Usage")
        output += filter(td, "Weak Foot Acc.", "Weak Foot Accuracy")
        output += filter(td, "Form")        
        output += filter(td, "Injury Resistance", "Injury Tolerance")        
        output += filter(td, "Ball Control")
        output += filter(td, "Dribbling")
        output += filter(td, "Finishing")
        output += filter(td, "Low Pass")
        output += filter(td, "Lofted Pass")
        output += filter(td, "Jump")
        output += filter(td, "Stamina")
        output += filter(td, "Speed")
        output += filter(td, "GK Catching", "Catching")
        output += filter(td, "GK Clearing", "Clearing")
        output += filter(td, "GK Reflexes", "Reflexes")
        output += filter(td, "GK Awareness", "Goalkeeping")
        output += filter(td, "GK Reach", "Coverage")
        output += filter(td, "Ball Winning")
        output += filter(td, "Heading", "Header")
        output += filter(td, "Defensive Awareness", "Defence Prowess")
        output += filter(td, "Kicking Power")
        output += filter(td, "Place Kicking")
        output += filter(td, "Physical Contact")
        output += filter(td, "Acceleration", "Explosive Power")
        output += filter(td, "Offensive Awareness", "Attacking Prowess")
        output += filter(td, "Curl", "Swerve")
        output += filter(td, "Balance", "Body Control")
        
    # etc
    div = soup.select('.player-main-column')
    
    output += "Player Styles: " + div[1].findChildren("li" , recursive=True)[0].text + "\n"
    
    output += "<Player Skills>\n"
    for li in div[2].findChildren("li" , recursive=True):
        s = get_pes2018_player_skills(li.text)
        
        if not s == "":
            output += s + "\n"
    
    output += "<COM Playing Styles>\n"
    for li in div[3].findChildren("li" , recursive=True):
        s = get_pes2018_com_skills(li.text)
        
        if not s == "":
            output += s + "\n"
            
    return output


# main
print_sep()
print((" " * 30) + "Welcome on Generate Infos for PES2018 Editor")
print_sep()
link = input("Enter with pes master player link: ")
print_sep()
print("Loading...")
print()

out = search_pes_master(link)
print(out)

print()
print("Copied infos to clipboard...")
pyperclip.copy(out)
spam = pyperclip.paste()
print()


