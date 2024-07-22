export const results = [
  {
    id: "1a2b3c4d-5678-90ab-cdef-1234567890ab",
    username: "john_doe",
    platform: "Twitter",
    content: "Just had the best coffee at the new cafe downtown! ☕️ #coffee #cafe #morning",
    date: "2024-07-08T08:30:00",
    likes: 120,
    comments: 15,
    shares: 10,
    tags: ["coffee", "cafe", "morning"],
  },
  {
    id: "2b3c4d5e-6789-01ab-cdef-2345678901bc",
    username: "jane_smith",
    platform: "Instagram",
    content: "Exploring the beautiful mountains this weekend. Nature is truly amazing! 🏞️ #hiking #nature #weekend",
    date: "2024-07-07T09:45:00",
    likes: 200,
    comments: 25,
    shares: 5,
    tags: ["hiking", "nature", "weekend"],
  },
  {
    id: "3c4d5e6f-7890-12ab-cdef-3456789012cd",
    username: "alice_jones",
    platform: "Facebook",
    content: "Had a wonderful dinner with friends last night. The food was fantastic! 🍽️ #dinner #friends #food",
    date: "2024-07-06T20:15:00",
    likes: 180,
    comments: 22,
    shares: 8,
    tags: ["dinner", "friends", "food"],
  },
  {
    id: "4d5e6f70-8901-23ab-cdef-4567890123de",
    username: "mike_brown",
    platform: "LinkedIn",
    content: "Excited to announce that I've started a new position at XYZ Corp! Looking forward to this new chapter. #career #newjob #excited",
    date: "2024-07-05T11:00:00",
    likes: 250,
    comments: 30,
    shares: 12,
    tags: ["career", "newjob", "excited"],
  },
  {
    id: "5e6f7071-9012-34ab-cdef-5678901234ef",
    username: "laura_green",
    platform: "Twitter",
    content: "Enjoying a peaceful evening walk in the park. 🌳 #eveningwalk #relaxation #nature",
    date: "2024-07-04T19:30:00",
    likes: 130,
    comments: 18,
    shares: 6,
    tags: ["eveningwalk", "relaxation", "nature"],
  },
  {
    id: "6f707172-0123-45ab-cdef-6789012345fg",
    username: "david_lee",
    platform: "Instagram",
    content: "Beach day with family! Loving the sun and the waves. 🏖️ #beachday #familytime #summer",
    date: "2024-07-03T14:00:00",
    likes: 220,
    comments: 28,
    shares: 10,
    tags: ["beachday", "familytime", "summer"],
  },
  {
    id: "70717273-1234-56ab-cdef-7890123456gh",
    username: "sarah_kim",
    platform: "Facebook",
    content: "Attended an inspiring workshop on personal development today. Feeling motivated! 💪 #workshop #personaldevelopment #inspiration",
    date: "2024-07-02T16:45:00",
    likes: 170,
    comments: 20,
    shares: 7,
    tags: ["workshop", "personaldevelopment", "inspiration"],
  },
  {
    id: "71727374-2345-67ab-cdef-8901234567hi",
    username: "tom_white",
    platform: "LinkedIn",
    content: "Sharing some insights from my recent project on sustainable energy solutions. 🌱 #sustainability #energy #innovation",
    date: "2024-07-01T10:15:00",
    likes: 260,
    comments: 35,
    shares: 14,
    tags: ["sustainability", "energy", "innovation"],
  },
  {
    id: "72737475-3456-78ab-cdef-9012345678ij",
    username: "emma_wilson",
    platform: "Twitter",
    content: "Reading an amazing book on mindfulness. Highly recommend it! 📖 #mindfulness #reading #selfcare",
    date: "2024-06-30T18:00:00",
    likes: 140,
    comments: 19,
    shares: 5,
    tags: ["mindfulness", "reading", "selfcare"],
  },
  {
    id: "73747576-4567-89ab-cdef-0123456789jk",
    username: "chris_adams",
    platform: "Instagram",
    content: "Captured this stunning sunset today. Nature's beauty is unparalleled. 🌅 #sunset #photography #nature",
    date: "2024-06-29T21:00:00",
    likes: 240,
    comments: 27,
    shares: 9,
    tags: ["sunset", "photography", "nature"],
  }
]


//export type Result = (typeof results)[number]

export const accounts = [
  {
    label: "Alicia Koch",
    email: "alicia@example.com",
    icon: (
      <svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <title>Vercel</title>
        <path d="M24 22.525H0l12-21.05 12 21.05z" fill="currentColor" />
      </svg>
    ),
  },
  {
    label: "Alicia Koch",
    email: "alicia@gmail.com",
    icon: (
      <svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <title>Gmail</title>
        <path
          d="M24 5.457v13.909c0 .904-.732 1.636-1.636 1.636h-3.819V11.73L12 16.64l-6.545-4.91v9.273H1.636A1.636 1.636 0 0 1 0 19.366V5.457c0-2.023 2.309-3.178 3.927-1.964L5.455 4.64 12 9.548l6.545-4.91 1.528-1.145C21.69 2.28 24 3.434 24 5.457z"
          fill="currentColor"
        />
      </svg>
    ),
  },
  {
    label: "Alicia Koch",
    email: "alicia@me.com",
    icon: (
      <svg role="img" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
        <title>iCloud</title>
        <path
          d="M13.762 4.29a6.51 6.51 0 0 0-5.669 3.332 3.571 3.571 0 0 0-1.558-.36 3.571 3.571 0 0 0-3.516 3A4.918 4.918 0 0 0 0 14.796a4.918 4.918 0 0 0 4.92 4.914 4.93 4.93 0 0 0 .617-.045h14.42c2.305-.272 4.041-2.258 4.043-4.589v-.009a4.594 4.594 0 0 0-3.727-4.508 6.51 6.51 0 0 0-6.511-6.27z"
          fill="currentColor"
        />
      </svg>
    ),
  },
]

export type Account = (typeof accounts)[number]

export const contacts = [
  {
    name: "Emma Johnson",
    email: "emma.johnson@example.com",
  },
  {
    name: "Liam Wilson",
    email: "liam.wilson@example.com",
  },
  {
    name: "Olivia Davis",
    email: "olivia.davis@example.com",
  },
  {
    name: "Noah Martinez",
    email: "noah.martinez@example.com",
  },
  {
    name: "Ava Taylor",
    email: "ava.taylor@example.com",
  },
  {
    name: "Lucas Brown",
    email: "lucas.brown@example.com",
  },
  {
    name: "Sophia Smith",
    email: "sophia.smith@example.com",
  },
  {
    name: "Ethan Wilson",
    email: "ethan.wilson@example.com",
  },
  {
    name: "Isabella Jackson",
    email: "isabella.jackson@example.com",
  },
  {
    name: "Mia Clark",
    email: "mia.clark@example.com",
  },
  {
    name: "Mason Lee",
    email: "mason.lee@example.com",
  },
  {
    name: "Layla Harris",
    email: "layla.harris@example.com",
  },
  {
    name: "William Anderson",
    email: "william.anderson@example.com",
  },
  {
    name: "Ella White",
    email: "ella.white@example.com",
  },
  {
    name: "James Thomas",
    email: "james.thomas@example.com",
  },
  {
    name: "Harper Lewis",
    email: "harper.lewis@example.com",
  },
  {
    name: "Benjamin Moore",
    email: "benjamin.moore@example.com",
  },
  {
    name: "Aria Hall",
    email: "aria.hall@example.com",
  },
  {
    name: "Henry Turner",
    email: "henry.turner@example.com",
  },
  {
    name: "Scarlett Adams",
    email: "scarlett.adams@example.com",
  },
]

export type Contact = (typeof contacts)[number]
