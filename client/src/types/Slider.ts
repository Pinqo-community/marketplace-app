export interface SliderProps<T> {
  items: T[];
  renderItem: (item: T) => JSX.Element;
  breakpoints: Record<number, { slidesPerView: number }>;
  slidesPerViewDefault: number;
  title: string;
  pagination?: boolean;
  customClassName?: string;
}
