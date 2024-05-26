<script setup lang="ts">
import { computed, type HTMLAttributes } from 'vue';
import { Search } from 'lucide-vue-next';
import { ComboboxInput, type ComboboxInputProps, useForwardProps } from 'radix-vue';
import { cn } from '@/lib/utils';
import { Input } from '@/components/ui/input';

defineOptions({
  inheritAttrs: false,
});

const props = defineProps<
  ComboboxInputProps & {
    class?: HTMLAttributes['class'];
    defaultValue?: string;
  }
>();

const delegatedProps = computed(() => {
  const { class: _, ...delegated } = props;

  return delegated;
});

const forwardedProps = useForwardProps(delegatedProps);
const inputText = defineModel<string>('inputText');
</script>

<template>
  <div class="flex items-center border-b px-3" cmdk-input-wrapper>
    <Search class="mr-2 h-4 w-4 shrink-0 opacity-50" />
    <ComboboxInput
      v-bind="{ ...forwardedProps, ...$attrs }"
      auto-focus
      as-child
      :class="
        cn(
          'flex h-11 w-full rounded-md bg-transparent py-3 text-sm outline-none placeholder:text-neutral-500 disabled:cursor-not-allowed disabled:opacity-50 ',
          props.class,
        )
      "
    >
      <Input
        v-model="inputText"
        :default-value="defaultValue"
        class="focus-visible:ring-0 border-none focus-visible:ring-offset-0"
      />
    </ComboboxInput>
  </div>
</template>
