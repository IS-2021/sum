function randomInt(min: number, max: number) {
  return Math.floor(Math.random() * (max - min) + min);
}

function formatHours(number: number) {
  return `${number.toString().padStart(2, "0")}:00`;
}

export function fakeOpeningHours() {
  return formatHours(randomInt(8, 12));
}

export function fakeClosingHours() {
  return formatHours(randomInt(18, 24));
}

export function getFakeHours() {
  if (Math.random() > 0.5) {
    return [fakeOpeningHours(), fakeClosingHours()];
  } else {
    return [];
  }
}
