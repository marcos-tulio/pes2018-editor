# -*- coding: utf-8 -*-
import os
import shutil
import sys

import requests
from bs4 import BeautifulSoup

url = ""
headers  = {}
response = None

def print_sep():
    print("{0:-<100}".format(""))

def search_transfermark(name):
    host = "https://www.transfermarkt.com.br"
    url  = host + '/schnellsuche/ergebnis/schnellsuche?query=' + name
    
    headers = {"User-Agent":"Mozilla/5.0"}

    response = requests.get(url, headers=headers)
    
    soup = BeautifulSoup(response.text, 'lxml')

    items = soup.select('.items > tbody')
    tr = items[0].findChildren("tr" , recursive=False)

    print("{0:-<100}".format(""))
    print("[{0}] {1: <30}{2: <8}{3: <30}{4: <15}{5}".format("#", "Nome", "Idade", "País", "Posição", "Clube"))
    print("{0:-<100}".format(""))

    players = [""] * len(tr)

    for i in range(len(tr)):
        td = tr[i].findChildren("td" , recursive=False)
        
        country = ""
        for c in td[4].find_all("img"):
            if c["alt"] == "Estados Unidos da América":
                c["alt"] = "EUA"
                
            country = c["alt"] + ", " + country
        
        td[0] = td[0].find_all("a", class_="spielprofil_tooltip")[0]
        
        print("[{0}] {1: <30}{2: <8}{3: <30}{4: <15}{5}".format(
            i,
            td[0].text,
            td[3].text,
            country[:len(country)-2],
            td[1].text,
            td[2].find_all("img")[0]["alt"],        
        ))
        
        players[i] = host + td[0]["href"]
    
    
    
    
    return players

def get_player_info(url):
    headers = {"User-Agent":"Mozilla/5.0"}

    response = requests.get(url, headers=headers)
    
    soup = BeautifulSoup(response.text, 'lxml')
    
    print (soup)

# main
print_sep()
print((" " * 30) + "Welcome on Generate Infos for PES2018 Editor")

# number, link
players = None

commands = ["q", "s"]
cmd = "s"
param = ""

while True:
    # quit
    if cmd == commands[0]:
        print("Bye!")
        exit()
      
    if cmd == commands[1]:
        # search player info
        if param == "":
            print_sep()
            name = input("Enter with player name to search: ")
            print("Searching...")
            print()

            players = search_transfermark(name)
            
            print()
            print("[{0}] {1: <30}".format("s", "research"))
            print("[{0}] {1: <30}".format("q", "quit"))
            
            param = ""
            
        # select player searched
        else:
            get_player_info(players[int(param)])
            players = None
            cmd = ""
            param = ""
        
    if cmd == "" or param == "":
        print()
        
        if cmd == "": 
            cmd = input("Enter with option: ") 
            
            # check cmd
            if not cmd in commands: 
                print("Invalid option!")
                cmd = ""
            
        elif param == "": 
            param = input("Enter with option: ")
            
            # cheack param
            if param in commands: 
                param = ""
        
        print()
    
    
    
    
    
    