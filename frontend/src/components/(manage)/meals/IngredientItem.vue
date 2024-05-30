<script setup lang="ts">
import type { IngredientDTO, Uuid } from '@/lib/api-model';
import { ref } from 'vue';

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
  mealId: Uuid;
  mealName: string;
  mealDescription: string;
  ingredients: IngredientDTO[] | undefined;
}>();

const amount = ref(0);
const { user } = useUser();

function deleteMeal() {
  deleteMealsId(props.mealId);
}
</script>

<template>
  <div class="bg-neutral-200 rounded p-4 my-4">
    <div class="flex justify-between">
      <h1 class="font-semibold text-xl">{{ props.mealName }}</h1>
      <p class="text-xs">Available amount: {{ amount }}</p>
    </div>
    <p class="text-neutral-950">{{ props.mealDescription }}</p>
    <div class="flex flex-wrap">
      <p class="text-neutral-800 pr-1">
        Ingredients: {{ props.ingredients?.map((ingredient) => ingredient.name).join(', ') }}
      </p>
    </div>
    <div v-if="user" class="mt-4">
      <Dialog>
        <DialogTrigger as-child>
          <Button size="icon" class="w-1/2">
            <Trash2 class="mr-1" />
            Delete
          </Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-md">
          <DialogHeader>
            <DialogTitle class="pb-4">Do you want to delete this meal?</DialogTitle>
          </DialogHeader>

          <DialogFooter class="sm:justify-start">
            <DialogClose as-child>
              <Button type="button" variant="secondary" @click="deleteMeal"> Delete meal </Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  </div>
</template>
