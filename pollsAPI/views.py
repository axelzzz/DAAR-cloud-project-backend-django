from pollsAPI.models import Question , Choice
from pollsAPI.serializers import QuestionSerializer, UserSerializer , ChoiceSerializer
from pollsAPI.permissions import IsOwnerOrReadOnly
from rest_framework import permissions, renderers, viewsets
from django.contrib.auth.models import User
from rest_framework.decorators import api_view, action
from rest_framework.response import Response
from rest_framework.reverse import reverse

@api_view(['GET'])
def api_root(request, format=None):
    return Response({
        'users': reverse('user-list', request=request, format=format),
        'questions': reverse('question-list', request=request, format=format)
    })

class ChoiceViewSet(viewsets.ModelViewSet):
    queryset = Choice.objects.all()
    serializer_class = ChoiceSerializer
    @action(detail=True, renderer_classes=[renderers.StaticHTMLRenderer])
    def perform_create(self, serializer):
        serializer.save(question=self.request.question)

class QuestionViewSet(viewsets.ModelViewSet):
    queryset = Question.objects.all()
    serializer_class = QuestionSerializer
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,
                          IsOwnerOrReadOnly,)

    @action(detail=True, renderer_classes=[renderers.StaticHTMLRenderer])
    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)

class UserViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
