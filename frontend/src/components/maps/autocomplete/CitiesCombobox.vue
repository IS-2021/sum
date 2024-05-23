<script setup lang="ts">
import { computed, ref } from 'vue';
import type { AutocompleteDTO } from '@/lib/api-model';

import { Check, ChevronsUpDown, LoaderCircleIcon } from 'lucide-vue-next';

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

interface CitiesComboboxProps {
  completions: AutocompleteDTO[];
}

const props = defineProps<CitiesComboboxProps>();

const open = ref(false);
const pickedPlaceId = ref('');

const emit = defineEmits<{
  searchChange: [payload: string];
  onSelect: [payload: string];
}>();

const citiesKey = computed(() => props.completions.map((city) => city.description).join(''));
</script>

<template>
  <Popover v-model:open="open">
    <PopoverTrigger as-child>
      <Button
        variant="outline"
        role="combobox"
        :aria-expanded="open"
        class="justify-between w-full max-w-screen-sm"
      >
        {{
          pickedPlaceId
            ? props.completions.find((completion) => completion.placeId === pickedPlaceId)
                ?.description
            : 'Search addresses...'
        }}
        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
      </Button>
    </PopoverTrigger>
    <PopoverContent class="p-0 min-w-max max-w-screen-sm w-full">
      <Command :key="citiesKey">
        <CommandInput
          class="h-9"
          placeholder="Search addresses..."
          v-on:input="(e) => emit('searchChange', e.target.value)"
        />
        <CommandEmpty class="grid justify-center">
          <LoaderCircleIcon class="animate-spin" />
        </CommandEmpty>
        <CommandList>
          <CommandGroup>
            <CommandItem
              v-for="completion in completions"
              :key="completion.placeId"
              :value="completion.placeId"
              @select="
                (ev) => {
                  if (typeof ev.detail.value === 'string') {
                    pickedPlaceId = ev.detail.value;
                    emit('onSelect', completion.placeId);
                  }
                  open = false;
                }
              "
            >
              {{ completion.description }}
              <Check
                :class="
                  cn(
                    'ml-auto h-4 w-4',
                    pickedPlaceId === completion.placeId ? 'opacity-100' : 'opacity-0',
                  )
                "
              />
            </CommandItem>
          </CommandGroup>
        </CommandList>
      </Command>
    </PopoverContent>
  </Popover>
</template>