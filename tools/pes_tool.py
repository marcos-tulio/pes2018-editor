# -*- coding: utf-8 -*-
import os
import shutil
import sys

def make_path_folders(base):
    for f in paths: 
        f = os.path.join(base, f)
        if not os.path.exists(f):
            os.makedirs(f)

def make_folders():
    if not os.path.exists(folder_editor):
        os.makedirs(folder_editor)
        
    if not os.path.exists(cpk_path_in):
        os.makedirs(cpk_path_in)
        
    make_path_folders(folder_database)

if not len(sys.argv) == 2:
    print(" Enter with 'dbtoeditor' or 'editortodb' or 'dbtocpk' or 'editortocpk' or 'makecpk'")
    exit()

mode = 0

if sys.argv[1]   == 'dbtoeditor':
    mode = 1

elif sys.argv[1] == 'editortodb':
    mode = 2
    
elif sys.argv[1] == 'dbtocpk':
    mode = 3
    
elif sys.argv[1] == 'editortocpk':
    mode = 4

elif sys.argv[1] == 'makecpk':
    mode = 5
    
else:
    print(" Enter with 'dbtoeditor' or 'editortodb' or 'dbtocpk' or 'editortocpk' or 'makecpk'")
    exit()

path_base = os.path.dirname(os.path.realpath(__file__));

#CPK
cpk_cmd  = os.path.join(os.path.join(path_base, "cpkmaker"), "cpkmakec.exe")
cpk_path_in  = os.path.join(path_base, "to_cpk")
cpk_path_out = os.path.join(path_base, "dt80_401U_ps3.cpk")
cpk_options  = "-align=2048 -mode=FILENAME -notoptocinfo -nolocalinfo -nodatetime"

# Editor and db
folder_database = os.path.join(path_base, "database")
folder_editor   = os.path.join(path_base, "editor_db")

paths = [
    "common\\etc\\pesdb", 
    "common\\character0\\model\\character\\appearance",
    "common\\character0\\model\\character\\glove",
    "common\\character0\\model\\character\\boots"
]

files = [
    paths[0] + "\\Player.bin",
    paths[0] + "\\Ball.bin",
    paths[0] + "\\BallCondition.bin",
    paths[0] + "\\Team.bin",
    paths[0] + "\\Country.bin",
    paths[0] + "\\Tactics.bin",
    paths[0] + "\\PlayerAssignment.bin",
    paths[0] + "\\TacticsFormation.bin",
    paths[0] + "\\Stadium.bin",
    paths[0] + "\\CompetitionRegulation.bin",
    paths[0] + "\\CompetitionEntry.bin",
    paths[0] + "\\Competition.bin",
    paths[0] + "\\CompetitionKind.bin",
    paths[0] + "\\Coach.bin",
    paths[0] + "\\Derby.bin",
    paths[0] + "\\StadiumOrder.bin",
    paths[0] + "\\StadiumOrderInConfederation.bin",
    paths[0] + "\\Boots.bin",
    paths[0] + "\\Glove.bin",
    paths[1] + "\\PlayerAppearance.bin",
    paths[2] + "\\GloveList.bin",
    paths[3] + "\\BootsList.bin",
]

make_folders()

# move files from db folder to editor folder
if mode == 1:
    for f in files:  
        full_path = f
        
        for p in paths:
            f = f.replace(p, "")
            
        f = f.replace("\\", "")   
        
        try:
            os.rename(os.path.join(folder_database, full_path), os.path.join(folder_editor, f))
            
        except:
            print("  -> not founded " + f)

# move files from editor folder to db folder
elif mode == 2:
    for f in files:
        full_path = f
        
        for p in paths:
            f = f.replace(p, "")
            
        f = f.replace("\\", "")
        
        try:
            os.rename(os.path.join(folder_editor, f), os.path.join(folder_database, full_path))
        except:
            print("  -> not founded " + f)

# copy files from db folder to cpk folder and make it
elif mode == 3:
    mode = 5
    
    make_path_folders(cpk_path_in)
    
    for f in files:  
        full_path = f
        
        for p in paths:
            f = f.replace(p, "")
            
        f = f.replace("\\", "")   
        
        try:
            dest = os.path.join(cpk_path_in, full_path)
            
            if os.path.exists(dest):            
                os.remove(dest)
                
            shutil.copyfile(os.path.join(folder_database, full_path), dest)
            
        except:
            print("  -> not founded " + f)

# copy files from editor folder to cpk folder and make it   
elif mode == 4:
    mode = 5
    
    make_path_folders(cpk_path_in)
    
    for f in files:  
        full_path = f
        
        for p in paths:
            f = f.replace(p, "")
            
        f = f.replace("\\", "")   
        
        try:
            dest = os.path.join(cpk_path_in, full_path)
            
            if os.path.exists(dest):            
                os.remove(dest)
                
            shutil.copyfile(os.path.join(folder_editor, f), dest)
            
        except:
            print("  -> not founded " + f)

# make cpk
if mode == 5:
    print ("  -> Building cpk...")
    os.system(cpk_cmd + " " + cpk_path_in + " " + cpk_path_out + " " + cpk_options)