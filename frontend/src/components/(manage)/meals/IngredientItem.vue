<script setup lang="ts">
import type { MealDTO, Uuid } from '@/lib/api-model';
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

import { deleteMealsId } from '@/lib/api/meals/meals';
import { useUser } from '@/composables/useUser';

const props = defineProps<{
  meal: MealDTO;
}>();

const mealId = props.meal.mealId;
const mealUpdateLink = `/manage/mealUpdate/${mealId}`;

const amount = ref(0);
const { user } = useUser();

function deleteMeal(id: Uuid) {
  deleteMealsId(id);
}
</script>

<template>
  <div class="bg-neutral-200 rounded p-4 my-4">
    <div class="flex justify-between">
      <h1 class="font-semibold text-xl">{{ props.meal.name }}</h1>
      <p class="text-xs">Available amount: {{ amount }}</p>
    </div>
    <p class="text-neutral-950">{{ props.meal.description }}</p>
    <div class="flex flex-wrap">
      <p class="text-neutral-800 pr-1">
        Ingredients: {{ props.meal.ingredients?.map((ingredient) => ingredient.name).join(', ') }}
      </p>
    </div>
    <div v-if="user" class="flex items-center gap-3 justify-between">
      <NavLink :to="mealUpdateLink" class="w-full">
        <Button class="w-1/2">Modify</Button>
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
</template>
