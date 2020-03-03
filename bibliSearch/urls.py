from django.urls import path

from . import views

app_name = 'bibliSearch'
urlpatterns = [
    path('', views.searchPattern, name='searchPattern'),
]