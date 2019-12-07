from django.shortcuts import get_object_or_404, render
from django.http import HttpResponseRedirect, HttpResponse,HttpRequest
from django.urls import reverse
from django.views import generic
#from django.http import Http404
from pollsAPI.models import Choice, Question
from django import forms
import datetime

def index(request):
    question_list = Question.objects.all()
    return render(request, 'polls/index.html', {'question_list': question_list})

def detail(request, question_id):
#    try:
#        question = Question.objects.get(pk=question_id)
#    except Question.DoesNotExist:
#        raise Http404("Question does not exist")
    question = get_object_or_404(Question, pk=question_id)
    return render(request, 'polls/detail.html', {'question': question})

#def results(request, question_id):
#    question = get_object_or_404(Question, pk=question_id)
#    return render(request, 'polls/results.html', {'question': question})
class ResultsView(generic.DetailView):
    model = Question
    template_name = 'polls/results.html'

def vote(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    try:
        selected_choice = question.choices.get(pk=request.POST['choice'])
    except (KeyError, Choice.DoesNotExist):
        return render(request, 'polls/detail.html', {
            'question': question,
            'error_message': "You didn't select a choice.",
        })
    else:
        selected_choice.votes += 1
        selected_choice.save()
        return HttpResponseRedirect(reverse('polls:results', args=(question.id,)))

def voteByPlaying(request, question_id):
    question = get_object_or_404(Question, pk=question_id)
    choiceids = str(list(question.choices.values_list('pk', flat=True)))[1:-1]
    try:
        selected_choice = question.choices.get(pk=request.POST['choice'])
    except (KeyError, Choice.DoesNotExist):
        return render(request, 'polls/voteByPlaying.html', {
            'question': question,
            'error_message': "You didn't finish the game.",
            'choiceids': choiceids,
        })
    else:
        selected_choice.votes += 1
        selected_choice.save()
        return HttpResponseRedirect(reverse('polls:results', args=(question.id,)))

def searchResponse(request):
    inp_value = request.GET.get('txt_search', 'This is a default value')
    context = {'inp_value': inp_value}
    return render( request, 'searchResult.html', context)