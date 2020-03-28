import os
import bibliSearch.models
import re

class Regex():

    def recherche(self, pattern, folder_path):       
        result = []
        for filename in os.listdir(folder_path):
            file_path = folder_path+"/"+filename
            with open(file_path,"r",encoding="UTF-8") as file:
                lines=re.split('\n', file.read())
                for line in lines :
                    if re.compile(pattern) .search(line):
                        result.append(bibliSearch.models.Book(file_path))
                        break

        return result
