from django.urls import path 

from . import views 

app_name = 'e_store'

urlpatterns = [
    path('',views.all_product, name='product_list'),
]
