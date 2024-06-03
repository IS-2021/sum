import { computed, type Ref, unref } from 'vue';

export function useRefOverride<T>(baseValue: Ref<T>, override: Ref<T | undefined | null>) {
  return computed(() => unref(override) || unref(baseValue));
}

export function useRefOverrideWithStatus<T>(
  baseValue: Ref<T>,
  override: Ref<T | undefined | null>,
) {
  const value = computed(() => unref(override) || unref(baseValue));
  const isOverridden = computed(() => unref(override) !== null && unref(override) !== undefined);

  return {
    value,
    isOverridden,
  };
}
