from rest_framework import serializers
from pollsAPI.models import Question, Choice
from django.contrib.auth.models import User

class ChoiceSerializer(serializers.ModelSerializer):
    question = serializers.ReadOnlyField(source='question.question_text')
    class Meta:
        model = Choice
        fields = ('question', 'choice_text', 'votes')

class QuestionSerializer(serializers.ModelSerializer):
    owner = serializers.ReadOnlyField(source='owner.username')
    choices = serializers.HyperlinkedRelatedField(many=True, view_name='choice-detail', read_only=True)
    class Meta:
        model = Question
        fields = ('url', 'id', 'owner', 'choices', 'question_text', 'pub_date', 'was_published_recently')

class UserSerializer(serializers.ModelSerializer):
    polls = serializers.HyperlinkedRelatedField(many=True, view_name='question-detail', read_only=True)
    class Meta:
        model = User
        fields = ('url', 'id', 'username', 'polls')
