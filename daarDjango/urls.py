from django.conf.urls import include, url
from django.contrib import admin
from django.urls import include, path
from rest_framework import routers

router = routers.DefaultRouter()
#router.register(r'users', views.UserViewSet)
#router.register(r'groups', views.GroupViewSet)

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    #path('admin/', include('admin.site.urls')),
    path('pollsAPI', include('pollsAPI.urls')),
    path('bibliSearch/', include('bibliSearch.urls')),
    
    path('snippets/', include('snippets.urls')),
    path('', include(router.urls)),
    path('api-auth/', include('rest_framework.urls', namespace='rest_framework'))
    
]
