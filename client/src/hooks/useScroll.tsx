import { useEffect, useState } from "react";

export const useScroll = () => {
  const [scrollPosition, setScrollPosition] = useState(0);
  const [isScrolledUp, setIsScrolledUp] = useState(false);

  useEffect(() => {
    let lastScrollPosition = 0;

    const handleScroll = () => {
      const currentPosition = window.scrollY;

      setIsScrolledUp(currentPosition < lastScrollPosition);
      lastScrollPosition = currentPosition;
      setScrollPosition(currentPosition);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return { scrollPosition, isScrolledUp };
};
