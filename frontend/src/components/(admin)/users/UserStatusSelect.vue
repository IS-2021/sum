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
import UserStatusIcon from '@/components/(admin)/users/UserStatusIcon.vue';

const props = defineProps<SelectRootProps>();
const emits = defineEmits<SelectRootEmits>();

const forwarded = useForwardPropsEmits(props, emits);
</script>

<template>
  <Select v-bind="forwarded">
    <SelectTrigger class="w-[240px]">
      <SelectValue placeholder="Account status" />
    </SelectTrigger>
    <SelectContent>
      <SelectGroup>
        <SelectLabel>Filter by status</SelectLabel>
        <SelectItem value="all">
          <CircleDashedIcon class="mr-2 inline-block h-4 w-4" /> All
        </SelectItem>
        <SelectItem value="Active">
          <UserStatusIcon class="mr-2 inline-block h-4 w-4" :is-banned="false" /> Active
        </SelectItem>
        <SelectItem value="Banned">
          <UserStatusIcon class="mr-2 inline-block h-4 w-4" :is-banned="true" /> Banned
        </SelectItem>
      </SelectGroup>
    </SelectContent>
  </Select>
</template>
