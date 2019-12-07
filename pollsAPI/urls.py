from django.conf.urls import url, include
from pollsAPI import views
from rest_framework.routers import DefaultRouter
from rest_framework.schemas import get_schema_view

schema_view = get_schema_view(title='Polls API')

# Create a router and register our viewsets with it.
router = DefaultRouter()
router.register(r'pollsAPI/questions', views.QuestionViewSet)
router.register(r'pollsAPI/choices', views.ChoiceViewSet)
router.register(r'pollsAPI/users', views.UserViewSet)

# The API URLs are now determined automatically by the router.
# Additionally, we include the login URLs for the browsable API.
urlpatterns = [
    url('^schema/$', schema_view),
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]
