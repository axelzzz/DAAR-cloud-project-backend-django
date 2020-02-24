from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse, JsonResponse
import json

def index(request):
    return render(request, 'bibliSearch/index.html')


def searchByTitle(request):
    #return render(request, 'bibliSearch/index.html')
    #return HttpResponse("you searched %s" %request.GET.get('book_title', ' hum '))
    json_books = {"books" : [request.GET.get('book_title'), "bible"]}
    return JsonResponse(json_books)
    #return HttpResponse(json.dumps(json_books), content_type="application/json")