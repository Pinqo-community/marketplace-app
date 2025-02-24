export interface BasePopupProps {
  isOpen: boolean;
  onClose: () => void;
  title?: string;
  titleTag?: "h2" | "h3";
  titleCentered?: boolean;
  variant?: "default" | "auth";
  children: React.ReactNode;
}
