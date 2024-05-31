<script setup lang="ts">
import { ImageField } from '@/components/(manage)/common/fields';
import ImagePreview from '@/components/(manage)/common/image/ImagePreview.vue';
import type { ImageChangeEvent, ImageFieldData } from '@/components/(manage)/common/fields/types';

const image = defineModel<ImageFieldData>('image', {
  default: {
    file: null,
    previewUrl: '',
  },
});

function setImage({ file, previewUrl }: ImageChangeEvent) {
  image.value = {
    file,
    previewUrl,
  };
}
</script>

<template>
  <ImageField @on-change="setImage" />

  <ImagePreview v-if="image.previewUrl" :src="image.previewUrl" />
</template>
