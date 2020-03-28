import os
import bibliSearch.models
import re

class Regex():


    def recherche(self, pattern, folder_path):       
        result = []
        for filename in os.listdir(folder_path):

            file_path = folder_path+"/"+filename
            current_file = open(file_path, mode="r",encoding="UTF-8")

            if(self.match(pattern, current_file)):
                result.append(bibliSearch.models.Book(file_path))

        return result



    def match(self, pattern, current_file):
        lines = current_file.read().split("\n")
        for line in lines :
            pattern = re.compile(pattern) 
            match = pattern.search(line) 
            if match:
                return True
        return False

