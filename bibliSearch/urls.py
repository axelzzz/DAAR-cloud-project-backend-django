from django.urls import path

from . import views

app_name = 'bibliSearch'
urlpatterns = [
    path('', views.index, name='index'),
    path('search/', views.searchByTitle, name='searchWithTitle'),
]