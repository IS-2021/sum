<script setup lang="ts">
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { type SelectRootEmits, type SelectRootProps, useForwardPropsEmits } from 'radix-vue';
import { BanIcon, CircleCheckBigIcon, CircleIcon, CircleDashedIcon } from 'lucide-vue-next';

const props = defineProps<SelectRootProps>();
const emits = defineEmits<SelectRootEmits>();

const forwarded = useForwardPropsEmits(props, emits);
</script>

<template>
  <Select v-bind="forwarded">
    <SelectTrigger class="w-[240px]">
      <SelectValue placeholder="Restaurant status" />
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectLabel>Filter by status</SelectLabel>
        <SelectItem value="all">
          <CircleDashedIcon class="mr-2 inline-block h-4 w-4" /> All
        </SelectItem>
        <SelectItem value="Active">
          <CircleCheckBigIcon class="mr-2 inline-block h-4 w-4" /> Active
        </SelectItem>
        <SelectItem value="Inactive">
          <CircleIcon class="mr-2 inline-block h-4 w-4" /> Inactive
        </SelectItem>
        <SelectItem value="Banned">
          <BanIcon class="mr-2 inline-block h-4 w-4" /> Banned
        </SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
</template>
