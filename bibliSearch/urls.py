from django.urls import path, re_path

from . import views

app_name = 'bibliSearch'
urlpatterns = [
    path('', views.getBooks, name='getBooks'),
    re_path('filter/$', views.filter, name='filter'),
]