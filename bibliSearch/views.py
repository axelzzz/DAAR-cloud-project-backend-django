from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse, JsonResponse
from py4j.java_gateway import JavaGateway
import json

def index(request):
    return render(request, 'bibliSearch/index.html')

def getNbLivres():
    #gateway = JavaGateway()
    #library = gateway.entry_point.getLibrary()
    #return library.size()
    return 10;


def searchPattern(request):
    #return render(request, 'bibliSearch/index.html')
    #return HttpResponse("you searched %s" %request.GET.get('book_title', ' hum '))
    #json_books = {"books" : [request.GET.get('book_title'), "bible"]}
    #return JsonResponse(json_books)
    #return HttpResponse(json.dumps(json_books), content_type="application/json")
    #return HttpResponse("il y a %i livres" %getNbLivres())
    return HttpResponse(getNbLivres())