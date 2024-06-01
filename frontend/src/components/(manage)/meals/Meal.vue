<script setup lang="ts">
import type { IngredientDTO, Uuid } from '@/lib/api-model';
import { Button } from '@/components/ui/button';
import { Trash2, Edit2Icon, OctagonAlertIcon } from 'lucide-vue-next';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';
import { deleteMealsId } from '@/lib/api/meals/meals';
import { useUser } from '@/composables/useUser';
import { computed } from 'vue';
import { cn } from '@/lib/utils';

const props = defineProps<{
  mealId: Uuid;
  mealName: string;
  mealDescription: string;
  ingredients: IngredientDTO[] | undefined;
}>();

const { user } = useUser();

async function deleteMeal() {
  const res = await deleteMealsId(props.mealId);

  if (res.status === 204) {
    location.reload();
  }
}

const hasAnyIngredients = computed(() =>
  props.ingredients ? props.ingredients?.length > 0 : false,
);
</script>

<template>
  <div class="bg-white border border-neutral-300 rounded p-4 my-4">
    <div :class="cn('flex items-center mb-2 gap-2', !hasAnyIngredients && 'text-red-500')">
      <h2 class="font-semibold text-xl">{{ mealName }}</h2>

      <TooltipProvider>
        <Tooltip>
          <TooltipTrigger>
            <OctagonAlertIcon v-if="!hasAnyIngredients" class="h-5 w-5" />
          </TooltipTrigger>
          <TooltipContent>
            <p>This meal won't be visible unless you set it's ingredients</p>
          </TooltipContent>
        </Tooltip>
      </TooltipProvider>
    </div>
    <p class="text-neutral-950 mb-4">{{ mealDescription }}</p>

    <p class="text-neutral-800 mb-4">
      Ingredients: {{ ingredients?.map((ingredient) => ingredient.name).join(', ') }}
    </p>

    <div v-if="user" class="justify-between flex gap-2">
      <Button variant="secondary" as-child>
        <RouterLink :to="`/manage/meals/edit/${mealId}`">
          <Edit2Icon class="inline-block mr-2 h-4 w-4" />
          Edit
        </RouterLink>
      </Button>

      <Dialog>
        <DialogTrigger as-child>
          <Button variant="outline-destructive">
            <Trash2 class="mr-2 h-4 w-4" />
            Delete
          </Button>
        </DialogTrigger>
        <DialogContent class="sm:max-w-md">
          <DialogHeader>
            <DialogTitle class="pb-4">Do you want to delete this meal?</DialogTitle>
          </DialogHeader>

          <DialogFooter class="sm:justify-start">
            <DialogClose as-child>
              <Button type="button" variant="destructive" @click="deleteMeal"> Delete meal </Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  </div>
</template>
