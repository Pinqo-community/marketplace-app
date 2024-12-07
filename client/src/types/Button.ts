export interface PrimaryButtonProps {
  children: React.ReactNode;
  light?: boolean;
}

export interface SliderButtonProps {
  prevRef: React.MutableRefObject<HTMLButtonElement | null>;
  nextRef: React.MutableRefObject<HTMLButtonElement | null>;
}

export interface MenuButtonProps {
  isOpened: boolean;
  toggleMenu: () => void;
}
