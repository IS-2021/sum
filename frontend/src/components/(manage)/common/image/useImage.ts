import { ref } from 'vue';
import type { ImageChangeEvent, ImageFieldData } from '@/components/(manage)/common/fields/types';

export function useImage() {
  const image = ref<ImageFieldData>({
    file: null,
    previewUrl: '',
  });

  function setImage({ file, previewUrl }: ImageChangeEvent) {
    image.value = {
      file,
      previewUrl,
    };
  }

  return {
    image,
    setImage,
  };
}
