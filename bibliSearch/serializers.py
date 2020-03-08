from rest_framework import serializers

class BookSerializer(serializers.Serializer):

    nameFile = serializers.CharField(max_length=200)
    title = serializers.CharField(max_length=200)
    author = serializers.CharField(max_length=200)
    postingDate = serializers.CharField(max_length=200)
    releaseDate = serializers.CharField(max_length=200)
    language = serializers.CharField(max_length=200)