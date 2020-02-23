from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse

def index(request):
    return render(request, 'bibliSearch/index.html')


def searchByTitle(request):
    return HttpResponse("you searched %s" %request.GET.get('book_title', ' hum '))