import os
import bibliSearch.models

class KMP():


    def recherche(self, pattern, folder_path):    
        result = []
        for filename in os.listdir(folder_path):

            file_path = folder_path+"/"+filename
            current_file = open(file_path, mode="r",encoding="UTF-8")

            lines = current_file.read().split("\n")
            for line in lines :
                if(self.match(pattern, line)==True):
                    #result.append(bibliSearch.models.Book(file_path))
                    result.append(filename)
                    break
        return result



    def match(self, pattern, line):
        test = ''
        if type(line) != type(test) or type(pattern) != type(test):
            return -1
        if len(pattern) == 0:
            return 0
        if len(line) == 0:
            return -1
        next = [-1]*len(pattern)
        if len(pattern) > 1:
            next[1] = 0
            i, j = 1, 0
            while i < len(pattern)-1:
                if j == -1 or pattern[i] == pattern[j]:
                    i += 1
                    j += 1
                    next[i] = j
                else:
                    j = next[j]

        m = s = 0
        while(s < len(pattern) and m < len(line)):
            if s == -1 or line[m] == pattern[s]:
                m += 1
                s += 1
            else:
                s = next[s]

        if s == len(pattern):
            return True

        return False

