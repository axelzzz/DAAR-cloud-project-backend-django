from django.conf.urls import include, url
from django.contrib import admin
from django.urls import include, path

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    #url(r'^polls/', include('polls.urls')),
    path('polls/', include('polls.urls')),
    path('bibliSearch/', include('bibliSearch.urls')),
    url(r'^snippets/', include('snippets.urls')),
    url(r'^pollsAPI/', include('pollsAPI.urls')),
]
