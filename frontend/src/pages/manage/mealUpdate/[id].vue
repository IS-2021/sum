<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';

import Button from '@/components/ui/button/Button.vue';
import { Trash2 } from 'lucide-vue-next';
import AddIngredientPopup from '@/components/(manage)/meals/AddIngredientPopup.vue';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { useUser } from '@/composables/useUser';
import { useRoute } from 'vue-router/auto';
import { useGetMealsId } from '@/lib/api/meals/meals';
import { computed, unref } from 'vue';

const route = useRoute('/manage/mealUpdate/[id]');
const mealId = route.params.id;

const { data } = useGetMealsId(mealId);
const meal = computed(() => unref(data)?.data);

const { user } = useUser();

function deleteIngredient(ingredientId: Uuid) {}
</script>

<template>
  <div class="flex justify-between">
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Update Meal</h1>
    <AddIngredientPopup v-if="user" :mealId="mealId" :userId="user.id" />
  </div>
  <div v-if="meal">
    <div v-if="meal.ingredients" class="space-y-3 mb-10 max-w-screen-md w-full">
      <p v-if="meal.ingredients.length === 0">No ingredients found</p>
      <div
        v-else
        v-for="ingredient in meal.ingredients"
        v-bind:key="ingredient.ingredientId"
        class="bg-neutral-200 rounded p-4 space-y-3"
      >
        <div class="flex justify-between">
          <h1 class="font-semibold text-xl">{{ ingredient.name }}</h1>
        </div>
        <p class="text-neutral-950">{{ ingredient.type }}</p>
        <div class="flex items-center gap-3 justify-between">
          <Dialog>
            <DialogTrigger as-child>
              <Trash2 class="cursor-pointer" />
            </DialogTrigger>
            <DialogContent class="sm:max-w-md">
              <DialogHeader>
                <DialogTitle class="pb-4">Do you want to delete this ingredient?</DialogTitle>
              </DialogHeader>

              <DialogFooter class="sm:justify-start">
                <DialogClose as-child>
                  <Button
                    type="button"
                    variant="secondary"
                    @click="deleteIngredient(ingredient.ingredientId)"
                  >
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
