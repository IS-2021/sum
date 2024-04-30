<script setup lang="ts">
import { ref } from 'vue';
import { useGetMeals } from '@/lib/api/meals/meals';
import { unref } from 'vue';
import { Button } from '@/components/ui/button';

const props = defineProps<{
  categories: string[]
}>()

const { data, isPending: areMealsLoading } = useGetMeals({ restaurantId: "08191b10-61ba-d389-a06d-8e7080548b41" });
const meals = unref(data)?.data;

const categories = ref(props.categories)
</script>

<template>
  <template v-if="areMealsLoading">
    <p>Loading...</p>
  </template>
  <div class="mt-10" v-for="category in categories" v-bind:key="category">
    <h1 class="text-2xl mb-4">{{ category }}</h1>
    <div v-for="meal in meals" v-bind:key="meal.id" class="w-full space-y-3">
      <div class="flex flex-col border min-w-80 rounded-lg my-3 p-3">
        <h1 class="font-semibold text-lg mb-4">{{ meal.name }}</h1>
        <p class="text-xs font-thin mb-8">{{ meal.description }}</p>
        <Button>Book</Button>
      </div>
    </div>
  </div>
</template>
