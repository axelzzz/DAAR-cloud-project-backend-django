import os
import bibliSearch.models

class Indexing():

    database_index_path = "bibliSearch/static/index"

    def recherche(self, pattern, folder_path):       
        result = []
        for filename in os.listdir(folder_path):

            file_path = folder_path+"/"+filename
            index_path = self.database_index_path+"/"+filename
            current_file = open(file_path, mode="r",encoding="UTF-8")
            current_index = open(index_path, mode="r",encoding="UTF-8")

            if(self.match(pattern, current_index)):
                result.append(bibliSearch.models.Book(file_path))

        return result



    def match(self, pattern, index_file):
        lines = index_file.read().split("\n")
        for line in lines :
            if pattern in line :
                return True
        return False

