export interface PrimaryButtonProps {
  children: React.ReactNode;
  light?: boolean;
}

export interface MenuButtonProps {
  isOpened: boolean;
  toggleMenu: () => void;
}

export interface SliderButtonProps {
  prevRef: React.MutableRefObject<HTMLButtonElement | null>;
  nextRef: React.MutableRefObject<HTMLButtonElement | null>;
}

export interface LocationButtonProps {
  hasError: boolean;
  locate: () => void;
}
