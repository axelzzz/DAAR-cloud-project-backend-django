from itertools import islice

class Parser() :

    NBLIGNES = 100

    def parseMetadata(self, b) :
        lines = b.getFile().read().split("\n")
        for i in range(0, self.NBLIGNES) :
            if("Title:" in lines[i]): 
                b.setTitle(lines[i].replace("Title: ", ""))
                
            elif("Author:" in lines[i]):
                b.setAuthor(lines[i].replace("Author: ", ""))
                
            elif("Posting Date:" in lines[i]):
                b.setPostingDate(lines[i].replace("Posting Date: ", ""))
                
            elif("Release Date:" in lines[i]):
                b.setReleaseDate(lines[i].replace("Release Date: ", ""))
                
            elif("Language:" in lines[i]):
                b.setLanguage(lines[i].replace("Language: ", ""))
