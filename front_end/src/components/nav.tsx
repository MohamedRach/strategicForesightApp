import { LucideIcon } from "lucide-react";
import { cn } from "../lib/utils";
import { buttonVariants } from "./ui/button";


interface navProps {
  links: {
    title: string,
    href: string,
    label?: string
    icon: LucideIcon
    variant: "default" | "ghost"
  }[]
}

export function Nav({links}: navProps) {

  return (
    <div
      className="fixed group flex flex-col gap-4 py-2 data-[collapsed=true]:py-2"
    >
      <div
            className={cn(
              "flex h-[52px] items-center justify-center",
              "px-2"
            )}
      >
          <p>Mohamed Sadek Rachidi</p>
      </div>

      <nav className="grid gap-1 px-2 group-[[data-collapsed=true]]:justify-center group-[[data-collapsed=true]]:px-2">
        {links.map((link, index) =>
         (
            <a
              key={index}
              href={`${link.href}`}
              className={cn(
                buttonVariants({ variant: link.variant, size: "sm" }),
                link.variant === "default" &&
                  "dark:bg-muted dark:text-white dark:hover:bg-muted dark:hover:text-white",
                "justify-start"
              )}
            >
              
              <link.icon className="mr-2 h-4 w-4" />
              {link.title}
              {link.label && (
                <span
                  className={cn(
                    "ml-auto",
                    link.variant === "default" &&
                      "text-background dark:text-white"
                  )}
                >
                  {link.label}
                </span>
              )}
            </a>
          )
        )}
      </nav>
    </div>
  )

}
