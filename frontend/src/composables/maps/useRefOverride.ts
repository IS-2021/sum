import { computed, type Ref, unref } from 'vue';

export function useRefOverride<T>(baseValue: Ref<T>, override: Ref<T | undefined | null>) {
  return computed(() => unref(override) || unref(baseValue));
}
