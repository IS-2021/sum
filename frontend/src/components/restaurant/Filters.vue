<script setup lang="ts">
import { Check, ChevronsUpDown } from 'lucide-vue-next';

import { ref } from 'vue';
import { cn } from '@/lib/utils';
import { Button } from '@/components/ui/button';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import TagsInput from './TagsInput.vue';

import { watch, type Ref } from 'vue';
import type { IngredientDTO, Uuid } from '@/lib/api-model';
import { getIngredients } from './restaurant';

const props = defineProps<{
  restaurantId: Uuid;
  unwantedIngredients: IngredientDTO[];
}>();

const emit = defineEmits<{
  (e: 'filterChange', unwantedIngredients: IngredientDTO[]): void;
}>();

let ingredients = getIngredients(props.restaurantId).ingredients;

const open = ref(false);
const unwantedIngredients: Ref<IngredientDTO[]> = ref(props.unwantedIngredients);

function updateFilter(element: IngredientDTO) {
  if (unwantedIngredients.value?.find((e) => e.name === element.name) !== undefined) {
    const index = unwantedIngredients.value?.indexOf(element);
    unwantedIngredients.value.splice(index, 1);
  } else {
    unwantedIngredients.value.push(element);
  }
}

watch(unwantedIngredients.value, (x) => {
  emit('filterChange', x);
});
</script>

<template>
  <TagsInput :unwantedIngredients="unwantedIngredients" />
  <Popover v-model:open="open">
    <PopoverTrigger as-child>
      <Button
        variant="outline"
        role="combobox"
        :aria-expanded="open"
        class="w-full justify-between text-white"
      >
        Select unwanted ingredients...

        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
      </Button>
    </PopoverTrigger>
    <PopoverContent class="w-full p-0">
      <Command>
        <CommandInput placeholder="Search ingredients..." />
        <CommandEmpty>No ingredients found.</CommandEmpty>
        <CommandList>
          <CommandGroup>
            <CommandItem
              v-for="ingredient in ingredients"
              :key="ingredient.id"
              :value="ingredient"
              :id="ingredient.id"
              @select="open = true"
              @click="updateFilter(ingredient)"
            >
              <Check
                :class="
                  cn(
                    'mr-2 h-4 w-4',
                    unwantedIngredients?.find((e) => e.name === ingredient.name)
                      ? 'opacity-100'
                      : 'opacity-0',
                  )
                "
              />
              {{ ingredient.name }}
            </CommandItem>
          </CommandGroup>
        </CommandList>
      </Command>
    </PopoverContent>
  </Popover>
</template>
