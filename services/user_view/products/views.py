from django.shortcuts import render
from django.http import HttpResponse
from rest_framework import viewsets, serializers
from rest_framework.response import Response

from .models import Products
from .serializers import ProductSerializer

class ProductViewSet(viewsets.ViewSet):
    def get_all(self, request):
        products = Products.objects.all() 
        serializer = ProductSerializer(products, many=True) 
        return Response(serializer.data)

    def get_wid(self, request, id):
        product = Products.objects.get(id=id)
        serializer = ProductSerializer(product)
        return Response(serializer.data)