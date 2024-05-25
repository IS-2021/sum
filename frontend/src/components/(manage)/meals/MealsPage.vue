<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { ref } from 'vue';
import NavLink from '@/components/(manage)/common/NavLink.vue';

import Button from '@/components/ui/button/Button.vue';
import { Trash2 } from 'lucide-vue-next';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';

import { getMeals } from '@/components/restaurant/restaurant';
import { deleteMealsId } from '@/lib/api/meals/meals';
import MealsUpdatePage from './MealsUpdatePage.vue';

const props = defineProps<{
  restaurantId: Uuid;
}>();

const meals = getMeals(props.restaurantId).meals;
const areMealsLoading = getMeals(props.restaurantId).areMealsLoading;
const amount = ref(0);

function deleteMeal(id: Uuid) {
  deleteMealsId(id);
}
</script>

<template>
  <template v-if="areMealsLoading">
    <p>Loading...</p>
  </template>
  <div v-else-if="meals">
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Meals</h1>
    <div class="space-y-3 mb-10 max-w-screen-md w-full">
      <p v-if="meals.length === 0">No meals found</p>
      <div
        v-else
        v-for="meal in meals"
        v-bind:key="meal.mealId"
        class="bg-neutral-200 rounded p-4 space-y-3"
      >
        <div class="flex justify-between">
          <h1 class="font-semibold text-xl">{{ meal.name }}</h1>
          <p class="text-xs">Available amount: {{ amount }}</p>
        </div>
        <p class="text-neutral-950">{{ meal.description }}</p>
        <div class="flex flex-wrap">
          <p class="text-neutral-800 pr-1">
            Ingredients: {{ meal.ingredients?.map((ingredient) => ingredient.name).join(', ') }}
          </p>
        </div>
        <div class="flex items-center gap-3 justify-between">
          <NavLink to="/manage/mealUpdate" class="w-full">
            <Button class="w-1/2">Modify</Button>
            <MealsUpdatePage
              v-if="meal.ingredients"
              class="hidden"
              :ingredients="meal.ingredients"
              :mealId="meal.mealId"
            />
          </NavLink>
          <Dialog>
            <DialogTrigger as-child>
              <Trash2 class="cursor-pointer" />
            </DialogTrigger>
            <DialogContent class="sm:max-w-md">
              <DialogHeader>
                <DialogTitle class="pb-4">Do you want to delete this meal?</DialogTitle>
              </DialogHeader>

              <DialogFooter class="sm:justify-start">
                <DialogClose as-child>
                  <Button type="button" variant="secondary" @click="deleteMeal(meal.mealId)">
                    Delete meal
                  </Button>
                </DialogClose>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>
      </div>
    </div>
  </div>
</template>
